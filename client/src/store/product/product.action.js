export function isLogin(value) {
    return {
        type: 'IS_LOGIN',
        payload: value
    }
}

export function getAllProductTypes(value) {
    return {
        type: 'GET_ALL_PRODUCT_TYPES',
        payload: value
    }
}

export function getAllProducts(value) {
    return {
        type: 'GET_ALL_PRODUCTS',
        payload: value
    }
}

export function getProductById(value) {
    return {
        type: 'GET_PRODUCT_BY_ID',
        payload: value
    }
}

export function getCart(value) {
    return {
        type: 'GET_CART',
        payload: value
    }
}

export function getProductFromCart(value) {
    return {
        type: 'GET_PRODUCT_FROM_CART',
        payload: value
    }
}

export function getOrder(value) {
    return {
        type: 'GET_ORDER',
        payload: value
    }
}

export function getAllOrders(value) {
    return {
        type: 'GET_ALL_ORDERS',
        payload: value
    }
}

export function getProductFromOrder(value) {
    return {
        type: 'GET_PRODUCT_FROM_ORDER',
        payload: value
    }
}

export function getAllUsers(value) {
    return {
        type: 'GET_ALL_USERS',
        payload: value
    }
}
