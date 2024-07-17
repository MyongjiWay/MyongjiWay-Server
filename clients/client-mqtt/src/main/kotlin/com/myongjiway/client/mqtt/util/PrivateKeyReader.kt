package com.myongjiway.client.mqtt.util

import org.apache.commons.codec.binary.Base64
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPrivateCrtKeySpec

/****************************************************************************
 * Amazon Modifications: Copyright 2016 Amazon.com, Inc. or its affiliates.
 * All Rights Reserved.
 *
 * Copyright (c) 1998-2010 AOL Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
// http://oauth.googlecode.com/svn/code/branches/jmeter/jmeter/src/main/java/org/apache/jmeter/protocol/oauth/sampler/PrivateKeyReader.java

/**
 * Class for reading RSA or ECC private key from PEM file.
 *
 * It can read PEM files with PKCS#8 or PKCS#1 encodings. It doesn't support
 * encrypted PEM files.
 */
object PrivateKeyReader {
    // Private key file using PKCS #1 encoding
    val P1_BEGIN_MARKER: String = "-----BEGIN RSA PRIVATE KEY" // $NON-NLS-1$
    val P1_END_MARKER: String = "-----END RSA PRIVATE KEY" // $NON-NLS-1$

    // Private key file using PKCS #8 encoding
    val P8_BEGIN_MARKER: String = "-----BEGIN PRIVATE KEY" // $NON-NLS-1$
    val P8_END_MARKER: String = "-----END PRIVATE KEY" // $NON-NLS-1$

    /**
     * Get a RSA Private Key from InputStream.
     *
     * @param fileName
     * file name
     * @return Private key
     * @throws IOException
     * IOException resulted from invalid file IO
     * @throws GeneralSecurityException
     * GeneralSecurityException resulted from invalid key format
     */
    @Throws(IOException::class, GeneralSecurityException::class)
    fun getPrivateKey(fileName: String?): PrivateKey? {
        FileInputStream(fileName).use { stream ->
            return getPrivateKey(stream, null)
        }
    }

    /**
     * Get a Private Key from InputStream.
     *
     * @param fileName
     * file name
     * @param algorithm
     * the name of the key algorithm, for example "RSA" or "EC"
     * @return Private key
     * @throws IOException
     * IOException resulted from invalid file IO
     * @throws GeneralSecurityException
     * GeneralSecurityException resulted from invalid key data
     */
    @Throws(IOException::class, GeneralSecurityException::class)
    fun getPrivateKey(fileName: String?, algorithm: String?): PrivateKey? {
        FileInputStream(fileName).use { stream ->
            return getPrivateKey(stream, algorithm)
        }
    }

    /**
     * Get a Private Key for the file.
     *
     * @param stream
     * InputStream object
     * @param algorithm
     * the name of the key algorithm, for example "RSA" or "EC"
     * @return Private key
     * @throws IOException
     * IOException resulted from invalid file IO
     * @throws GeneralSecurityException
     * GeneralSecurityException resulted from invalid key data
     */
    @Throws(IOException::class, GeneralSecurityException::class)
    fun getPrivateKey(stream: InputStream?, algorithm: String?): PrivateKey? {
        var key: PrivateKey? = null
        var isRSAKey = false

        val br = BufferedReader(InputStreamReader(stream, "UTF-8"))
        val builder = StringBuilder()
        var inKey = false
        var line = br.readLine()
        while (line != null) {
            if (!inKey) {
                if (line.startsWith("-----BEGIN ") && line.endsWith(" PRIVATE KEY-----")) {
                    inKey = true
                    isRSAKey = line.contains("RSA")
                }
                line = br.readLine()
                continue
            } else {
                if (line.startsWith("-----END ") && line.endsWith(" PRIVATE KEY-----")) {
                    inKey = false
                    isRSAKey = line.contains("RSA")
                    break
                }
                builder.append(line)
            }
            line = br.readLine()
        }
        var keySpec: KeySpec? = null
        val encoded = Base64.decodeBase64(builder.toString())
        if (isRSAKey) {
            keySpec = getRSAKeySpec(encoded)
        } else {
            keySpec = PKCS8EncodedKeySpec(encoded)
        }
        val kf = KeyFactory.getInstance(if ((algorithm == null)) "RSA" else algorithm)
        key = kf.generatePrivate(keySpec)

        return key
    }

    /**
     * Convert PKCS#1 encoded private key into RSAPrivateCrtKeySpec.
     *
     *
     *
     * The ASN.1 syntax for the private key with CRT is
     *
     * <pre>
     * --
     * -- Representation of RSA private key with information for the CRT algorithm.
     * --
     * RSAPrivateKey ::= SEQUENCE {
     * version           Version,
     * modulus           INTEGER,  -- n
     * publicExponent    INTEGER,  -- e
     * privateExponent   INTEGER,  -- d
     * prime1            INTEGER,  -- p
     * prime2            INTEGER,  -- q
     * exponent1         INTEGER,  -- d mod (p-1)
     * exponent2         INTEGER,  -- d mod (q-1)
     * coefficient       INTEGER,  -- (inverse of q) mod p
     * otherPrimeInfos   OtherPrimeInfos OPTIONAL
     * }
     </pre> *
     *
     * @param keyBytes
     * PKCS#1 encoded key
     * @return KeySpec
     * @throws IOException
     * IOException resulted from invalid file IO
     */
    @Throws(IOException::class)
    private fun getRSAKeySpec(keyBytes: ByteArray): RSAPrivateCrtKeySpec {
        var parser = DerParser(keyBytes)

        val sequence = parser.read()
        if (sequence.type != DerParser.SEQUENCE) throw IOException("Invalid DER: not a sequence") // $NON-NLS-1$

        // Parse inside the sequence
        parser = sequence.parser

        parser.read() // Skip version
        val modulus = parser.read().integer
        val publicExp = parser.read().integer
        val privateExp = parser.read().integer
        val prime1 = parser.read().integer
        val prime2 = parser.read().integer
        val exp1 = parser.read().integer
        val exp2 = parser.read().integer
        val crtCoef = parser.read().integer

        val keySpec = RSAPrivateCrtKeySpec(
            modulus,
            publicExp,
            privateExp,
            prime1,
            prime2,
            exp1,
            exp2,
            crtCoef,
        )

        return keySpec
    }
}

/**
 * A bare-minimum ASN.1 DER decoder, just having enough functions to decode
 * PKCS#1 private keys. Especially, it doesn't handle explicitly tagged types
 * with an outer tag.
 *
 *
 *
 * This parser can only handle one layer. To parse nested constructs, get a new
 * parser for each layer using `Asn1Object.getParser()`.
 *
 *
 *
 * There are many DER decoders in JRE but using them will tie this program to a
 * specific JCE/JVM.
 *
 * @author zhang
 */
internal class DerParser(
    /**
     * Create a new DER decoder from an input stream.
     *
     * @param in
     * The DER encoded stream
     */
    protected var `in`: InputStream,
) {
    /**
     * Create a new DER decoder from a byte array.
     *
     * @param bytes
     * encoded bytes
     * @throws IOException
     * IOException resulted from invalid file IO
     */
    constructor(bytes: ByteArray?) : this(ByteArrayInputStream(bytes))

    /**
     * Read next object. If it's constructed, the value holds encoded content
     * and it should be parsed by a new parser from
     * `Asn1Object.getParser`.
     *
     * @return A object
     * @throws IOException
     * IOException resulted from invalid file IO
     */
    @Throws(IOException::class)
    fun read(): Asn1Object {
        val tag = `in`.read()

        if (tag == -1) throw IOException("Invalid DER: stream too short, missing tag") // $NON-NLS-1$

        val length = length

        val value = ByteArray(length)
        val n = `in`.read(value)
        if (n < length) throw IOException("Invalid DER: stream too short, missing value") // $NON-NLS-1$

        val o = Asn1Object(tag, length, value)

        return o
    }

    @get:Throws(IOException::class)
    private val length: Int
        /**
         * Decode the length of the field. Can only support length encoding up to 4
         * octets.
         *
         *
         *
         * In BER/DER encoding, length can be encoded in 2 forms,
         *
         *  * Short form. One octet. Bit 8 has value "0" and bits 7-1 give the
         * length.
         *  * Long form. Two to 127 octets (only 4 is supported here). Bit 8 of
         * first octet has value "1" and bits 7-1 give the number of additional
         * length octets. Second and following octets give the length, base 256,
         * most significant digit first.
         *
         *
         * @return The length as integer
         * @throws IOException
         * IOException resulted from invalid file IO
         */
        get() {
            val i = `in`.read()
            if (i == -1) throw IOException("Invalid DER: length missing") // $NON-NLS-1$

            // A single byte short length
            if ((i and 0x7F.inv()) == 0) return i

            val num = i and 0x7F

            // We can't handle length longer than 4 bytes
            if (i >= 0xFF || num > 4) {
                throw IOException(
                    "Invalid DER: length field too big (" + // $NON-NLS-1$
                        i + ")",
                ) // $NON-NLS-1$
            }

            val bytes = ByteArray(num)
            val n = `in`.read(bytes)
            if (n < num) throw IOException("Invalid DER: length too short") // $NON-NLS-1$

            return BigInteger(1, bytes).toInt()
        }

    companion object {
        // Classes
        val UNIVERSAL: Int = 0x00
        val APPLICATION: Int = 0x40
        val CONTEXT: Int = 0x80
        val PRIVATE: Int = 0xC0

        // Constructed Flag
        val CONSTRUCTED: Int = 0x20

        // Tag and data types
        val ANY: Int = 0x00
        val BOOLEAN: Int = 0x01
        val INTEGER: Int = 0x02
        val BIT_STRING: Int = 0x03
        val OCTET_STRING: Int = 0x04
        val NULL: Int = 0x05
        val OBJECT_IDENTIFIER: Int = 0x06
        val REAL: Int = 0x09
        val ENUMERATED: Int = 0x0a
        val RELATIVE_OID: Int = 0x0d

        val SEQUENCE: Int = 0x10
        val SET: Int = 0x11

        val NUMERIC_STRING: Int = 0x12
        val PRINTABLE_STRING: Int = 0x13
        val T61_STRING: Int = 0x14
        val VIDEOTEX_STRING: Int = 0x15
        val IA5_STRING: Int = 0x16
        val GRAPHIC_STRING: Int = 0x19
        val ISO646_STRING: Int = 0x1A
        val GENERAL_STRING: Int = 0x1B

        val UTF8_STRING: Int = 0x0C
        val UNIVERSAL_STRING: Int = 0x1C
        val BMP_STRING: Int = 0x1E

        val UTC_TIME: Int = 0x17
        val GENERALIZED_TIME: Int = 0x18
    }
}

/**
 * An ASN.1 TLV. The object is not parsed. It can only handle integers and
 * strings.
 *
 * @author zhang
 */
internal class Asn1Object(protected val tag: Int, val length: Int, val value: ByteArray) {
    val type: Int

    /**
     * Construct a ASN.1 TLV. The TLV could be either a constructed or primitive
     * entity.
     *
     *
     *
     * The first byte in DER encoding is made of following fields,
     *
     * <pre>
     * -------------------------------------------------
     * |Bit 8|Bit 7|Bit 6|Bit 5|Bit 4|Bit 3|Bit 2|Bit 1|
     * -------------------------------------------------
     * |  Class    | CF  |     +      Type             |
     * -------------------------------------------------
     </pre> *
     *
     *
     *  * Class: Universal, Application, Context or Private
     *  * CF: Constructed flag. If 1, the field is constructed.
     *  * Type: This is actually called tag in ASN.1. It indicates data type
     * (Integer, String) or a construct (sequence, choice, set).
     *
     *
     * @param tag
     * Tag or Identifier
     * @param length
     * Length of the field
     * @param value
     * Encoded octet string for the field.
     */
    init {
        this.type = tag and 0x1F
    }

    val isConstructed: Boolean
        get() = (tag and DerParser.CONSTRUCTED) == DerParser.CONSTRUCTED

    @get:Throws(IOException::class)
    val parser: DerParser
        /**
         * For constructed field, return a parser for its content.
         *
         * @return A parser for the construct.
         * @throws IOException
         * IOException resulted from invalid file IO
         */
        get() {
            if (!isConstructed) throw IOException("Invalid DER: can't parse primitive entity") // $NON-NLS-1$

            return DerParser(value)
        }

    @get:Throws(IOException::class)
    val integer: BigInteger
        /**
         * Get the value as integer
         *
         * @return BigInteger
         * @throws IOException
         * IOException resulted from invalid file IO
         */
        get() {
            if (type != DerParser.INTEGER) throw IOException("Invalid DER: object is not integer") // $NON-NLS-1$

            return BigInteger(value)
        }

    @get:Throws(IOException::class)
    val string: String
        /**
         * Get value as string. Most strings are treated as Latin-1.
         *
         * @return Java string
         * @throws IOException
         * IOException resulted from invalid file IO
         */
        get() {
            val encoding: String

            when (type) {
                DerParser.NUMERIC_STRING, DerParser.PRINTABLE_STRING, DerParser.VIDEOTEX_STRING, DerParser.IA5_STRING, DerParser.GRAPHIC_STRING, DerParser.ISO646_STRING, DerParser.GENERAL_STRING -> encoding = "ISO-8859-1" // $NON-NLS-1$
                DerParser.BMP_STRING -> encoding = "UTF-16BE" // $NON-NLS-1$
                DerParser.UTF8_STRING -> encoding = "UTF-8" // $NON-NLS-1$
                DerParser.UNIVERSAL_STRING -> throw IOException("Invalid DER: can't handle UCS-4 string") // $NON-NLS-1$

                else -> throw IOException("Invalid DER: object is not a string") // $NON-NLS-1$
            }
            return String(value, charset(encoding))
        }
}
