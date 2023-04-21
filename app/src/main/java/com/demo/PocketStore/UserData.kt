package com.demo.PocketStore

class UserData {


    //get username
    var userName //username
            : String? = null


    var userEmail //email
            : String? = null


    var userPhone //phone number
            : String? = null


    var userPwd //password
            : String? = null


    var userStatus
            : String? = null


    var userPro: String? = null

    //config userid
    //get userid
    var userId //user ID
            = 0
    var pwdresetFlag = 0

    constructor(userName: String?, userPwd: String?) : super() {  //only username and password
        this.userName = userName
        this.userPwd = userPwd
    }

    constructor(
        userName: String?,
        userPwd: String?,
        userEmail: String?,
        userPhone: String?
    ) : super() {  //only username and password
        this.userName = userName
        this.userPwd = userPwd
        this.userEmail = userEmail
        this.userPhone = userPhone
    }

    constructor(
        id: Int, userName: String?, userEmail: String?, userPhone: String?,
        userPro: String?, userStatus: String?, userPwd: String?
    ) : super() {  //only username and password
        userId = id
        this.userName = userName
        this.userPwd = userPwd
        this.userEmail = userEmail
        this.userPhone = userPhone
        this.userStatus = userStatus
        this.userPro = userPro
    }

    constructor() : super() {  //only username and password
    }
}