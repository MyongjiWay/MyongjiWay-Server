package com.myongjiway.storage.db.core

import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class ExampleEntity(
    @Column
    val exampleColumn: String,
) : BaseEntity()
