const initState = {
    isLogin: false,
    users: [],
    types: [],
    products: [],
    product: {},
    user: {},
    cart: {},
    productFromCart: [],
    orders: [],
    order: {},
    productFromOrder: []
}
export const productReducer = (state = initState, action) => {
    switch (action.type) {
        case 'IS_LOGIN':
            return {
                ...state,
                isLogin: action.payload
            }
        case 'GET_ALL_USERS':
            return {
                ...state,
                users: action.payload
            }
        case 'GET_ALL_PRODUCT_TYPES':
            return {
                ...state,
                types: action.payload
            }
        case 'GET_ALL_PRODUCTS':
            return {
                ...state,
                products: action.payload
            }
        case 'GET_PRODUCT_BY_ID':
            return {
                ...state,
                product: action.payload
            }
        case 'GET_CART':
            return {
                ...state,
                cart: action.payload
            }
        case 'GET_PRODUCT_FROM_CART':
            return {
                ...state,
                productFromCart: action.payload
            }
        case 'GET_ORDER':
            return {
                ...state,
                order: action.payload
            }
        case 'GET_ALL_ORDERS':
            return {
                ...state,
                orders: action.payload
            }
        case 'GET_PRODUCT_FROM_ORDER':
            return {
                ...state,
                productFromOrder: action.payload
            }
        default:
            return state
    }
}