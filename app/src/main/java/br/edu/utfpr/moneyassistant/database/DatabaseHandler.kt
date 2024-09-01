package br.edu.utfpr.moneyassistant.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.moneyassistant.model.Register
import java.text.SimpleDateFormat
import java.util.Locale

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
            val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'XXX yyyy", Locale.ENGLISH)
            val date = formatter.parse(rawRegisters.getString(DATE_COLUMN_INDEX))

            registers.add(Register(
                _id = rawRegisters.getInt(ID_COLUMN_INDEX),
                type = rawRegisters.getString(TYPE_COLUMN_INDEX),
                detail = rawRegisters.getString(DETAIL_COLUMN_INDEX),
                value = rawRegisters.getInt(VALUE_COLUMN_INDEX),
                date = date!!,
            ))
        }

        return registers
    }

    fun countValues(): Int {
        val registers = this.list()
        val (credits, debits) = registers.partition {
            it.type == "Crédito"
        }
        val creditValues = credits.fold(0) {
                                           acc, e -> acc + e.value
        }
        val debitValues = debits.fold(0) {
                acc, e -> acc + e.value
        }

        return creditValues - debitValues
    }
}