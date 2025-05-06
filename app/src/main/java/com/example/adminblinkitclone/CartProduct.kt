package com.example.adminblinkitclone




data class CartProduct(

    val productId: String = "random", // can't apply nullability check
    val productTitle : String ? = null,
    var productImage : String ? = null,
    val productPrice : String ? = null,
    val productQuantity : String ? = null,
    var productType : String ? = null,
    var productCount : Int ? = null,
    var productStock : Int ? = null,
    var productCategory : String ? = null,
    var adminUId : String ? = null,

)