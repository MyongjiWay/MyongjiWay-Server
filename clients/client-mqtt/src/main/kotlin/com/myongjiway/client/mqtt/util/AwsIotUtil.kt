package com.myongjiway.client.mqtt.util

import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory

object AwsIotUtil {
    class KeyStorePasswordPair(val keyStore: KeyStore, val keyPassword: String)

    fun getKeyStorePasswordPair(certificateFile: String, privateKeyFile: String): KeyStorePasswordPair? = getKeyStorePasswordPair(certificateFile, privateKeyFile, null)

    fun getKeyStorePasswordPair(
        certificateFile: String,
        privateKeyFile: String,
        keyAlgorithm: String?,
    ): KeyStorePasswordPair? {
        if (certificateFile.isEmpty() || privateKeyFile.isEmpty()) {
            println("Certificate or private key file missing")
            return null
        }
        println("Cert file: $certificateFile Private key: $privateKeyFile")

        val privateKey = loadPrivateKeyFromFile(privateKeyFile, keyAlgorithm)
        val certChain = loadCertificatesFromFile(certificateFile)

        return if (certChain == null || privateKey == null) null else getKeyStorePasswordPair(certChain, privateKey)
    }

    fun getKeyStorePasswordPair(certificates: List<Certificate>, privateKey: PrivateKey): KeyStorePasswordPair? = try {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)

        // randomly generated key password for the key in the KeyStore
        val keyPassword = BigInteger(128, SecureRandom()).toString(32)

        val certChain = certificates.toTypedArray()
        keyStore.setKeyEntry("alias", privateKey, keyPassword.toCharArray(), certChain)

        KeyStorePasswordPair(keyStore, keyPassword)
    } catch (e: KeyStoreException) {
        println("Failed to create key store")
        null
    } catch (e: NoSuchAlgorithmException) {
        println("Failed to create key store")
        null
    } catch (e: CertificateException) {
        println("Failed to create key store")
        null
    } catch (e: IOException) {
        println("Failed to create key store")
        null
    }

    private fun loadCertificatesFromFile(filename: String): List<Certificate>? {
        val file = File(filename)
        if (!file.exists()) {
            println("Certificate file: $filename is not found.")
            return null
        }

        return try {
            BufferedInputStream(FileInputStream(file)).use { stream ->
                val certFactory = CertificateFactory.getInstance("X.509")
                certFactory.generateCertificates(stream) as List<Certificate>
            }
        } catch (e: IOException) {
            println("Failed to load certificate file $filename")
            null
        } catch (e: CertificateException) {
            println("Failed to load certificate file $filename")
            null
        }
    }

    private fun loadPrivateKeyFromFile(filename: String, algorithm: String?): PrivateKey? {
        val file = File(filename)
        if (!file.exists()) {
            println("Private key file not found: $filename")
            return null
        }

        return try {
            DataInputStream(FileInputStream(file)).use { stream ->
                PrivateKeyReader.getPrivateKey(stream, algorithm)
            }
        } catch (e: IOException) {
            println("Failed to load private key from file $filename")
            null
        } catch (e: GeneralSecurityException) {
            println("Failed to load private key from file $filename")
            null
        }
    }
}
