package com.piashcse.utils

class UserNotExistException : Exception()
class EmailNotExist : Exception()
class PasswordDoesNotMatch : Exception()
class CommonException(itemName: String) : Exception(itemName)

