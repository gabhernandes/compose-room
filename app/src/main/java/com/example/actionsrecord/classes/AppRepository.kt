package com.example.actionsrecord.classes

class AppRepository(private val myActionDao: MyActionDao) {

    fun insertAction(action: MyAction) {
        myActionDao.insertAction(action)
    }

    fun updateAction(action: MyAction) {
        myActionDao.updateAction(action)
    }

    fun getAllActions(): List<MyAction> {
        return myActionDao.getAllActions()
    }

}
