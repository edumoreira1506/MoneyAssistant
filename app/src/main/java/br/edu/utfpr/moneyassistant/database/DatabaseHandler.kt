package br.edu.utfpr.moneyassistant.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.moneyassistant.model.Register
import java.util.Date

class DatabaseHandler (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, detail TEXT, value INTEGER, register_date TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "registers"
        public const val ID_COLUMN_INDEX = 0
        public const val TYPE_COLUMN_INDEX = 1
        public const val DETAIL_COLUMN_INDEX = 2
        public const val VALUE_COLUMN_INDEX = 3
        public const val DATE_COLUMN_INDEX = 4
    }

    fun insert(register: Register) {
        val database = this.writableDatabase
        val newRegister = ContentValues()

        newRegister.put("value",register.value)
        newRegister.put("type", register.type)
        newRegister.put("detail", register.detail)
        newRegister.put("register_date", register.date.toString())

        database.insert(TABLE_NAME, null, newRegister)
    }

    fun update(register: Register) {
        val database = this.writableDatabase
        val updatedRegister = ContentValues()

        updatedRegister.put("value",register.value)
        updatedRegister.put("type", register.type)
        updatedRegister.put("detail", register.detail)
        updatedRegister.put("register_date", register.date.toString())

        database.update(
            TABLE_NAME,
            updatedRegister,
            "_id=${register._id}",
            null
        )
    }

    fun delete(id: Int) {
        val database = this.writableDatabase

        database.delete(TABLE_NAME, "_id=${id}", null)
    }

    fun find(id: Int): Register? {
        val database = this.writableDatabase
        val register = database.query(
            "registers",
            null,
            "_id=${id}",
            null,
            null,
            null,
            null
        )

        return if (register.moveToNext()) {
            Register(
                _id = id,
                type = register.getString(TYPE_COLUMN_INDEX),
                detail = register.getString(DETAIL_COLUMN_INDEX),
                value = register.getInt(VALUE_COLUMN_INDEX),
                date = Date(Date.parse(register.getString(DATE_COLUMN_INDEX))),
            )
        } else {
            null
        }
    }

    fun list(): MutableList<Register> {
        val database = this.writableDatabase
        val rawRegisters = database.query(
            "registers",
            null,
            null,
            null,
            null,
            null,
            null
        )
        val registers = mutableListOf<Register>()

        while(rawRegisters.moveToNext()) {
            registers.add(Register(
                _id = rawRegisters.getInt(ID_COLUMN_INDEX),
                type = rawRegisters.getString(TYPE_COLUMN_INDEX),
                detail = rawRegisters.getString(DETAIL_COLUMN_INDEX),
                value = rawRegisters.getInt(VALUE_COLUMN_INDEX),
                date = Date(Date.parse(rawRegisters.getString(DATE_COLUMN_INDEX))),
            ))
        }

        registers.add(Register(
            _id = 1,
            type = "CREDIT",
            detail = "Saldo",
            value = 10,
            date = Date(2024, 1,1)
        ))

        registers.add(Register(
            _id = 2,
            type = "DEBIT",
            detail = "Aaaaaa",
            value = 10,
            date = Date(2024, 1,1)
        ))

        return registers
    }

    fun cursorList(): Cursor {
        val database = this.writableDatabase
        val registers = database.query(
            "registers",
            null,
            null,
            null,
            null,
            null,
            null
        )

        return registers
    }
}