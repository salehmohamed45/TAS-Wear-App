# TAS Collection - Men's Clothing E-commerce Android App

A native Android application for "TAS Collection" (Men's Clothing E-commerce) built with Kotlin and Jetpack Compose.

## 🏗️ Architecture

This project follows **MVVM (Model-View-ViewModel)** architecture with **Manual Dependency Injection** (no Hilt/Dagger).

### Tech Stack

- **Framework**: Jetpack Compose with Material 3
- **Language**: Kotlin
- **Authentication**: Firebase Authentication (Email/Password)
- **Database**: Cloud Firestore
- **Architecture**: MVVM
- **Dependency Injection**: Manual DI via AppContainer
- **Navigation**: Jetpack Navigation Compose

## 📦 Project Structure

```
com.depi.taswear/
├── data/
│   ├── model/              # Data models (User, Product, Order, CartItem)
│   ├── repository/         # Repositories (Auth, Product, Order)
│   └── AppContainer.kt     # Manual DI container
├── ui/
│   ├── screens/            # Compose UI screens
│   ├── viewmodel/          # ViewModels with state management
│   ├── navigation/         # Navigation setup
│   └── theme/              # Material 3 theme
└── TASWearApplication.kt   # Application class
```

## 🔑 Key Components

### 1. AppContainer (Manual DI)
Located at `data/AppContainer.kt`, this class manages:
- Firebase Auth instance
- Firestore instance
- All repositories (AuthRepository, ProductRepository, OrderRepository)

### 2. ViewModelFactory
Custom `ViewModelProvider.Factory` that injects dependencies from AppContainer into ViewModels.

### 3. Data Models

#### User
```kotlin
data class User(
    val uid: String,
    val email: String,
    val role: String  // "customer" or "admin"
)
```

#### Product
```kotlin
data class Product(
    val id: String,
    val sku: String,
    val name: String,
    val description: String,
    val price: Double,
    val stockQty: Int,
    val category: String,
    val imageUrl: String,
    val isFeatured: Boolean
)
```

#### Order
```kotlin
data class Order(
    val orderId: String,
    val userId: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val status: String,  // "pending", "processing", "completed", "cancelled"
    val createdAt: Long
)
```

#### CartItem
```kotlin
data class CartItem(
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val quantity: Int,
    val imageUrl: String
)
```

### 4. Repositories

#### AuthRepository
- `signIn(email, password)`: Authenticate user
- `signUp(email, password, role)`: Create new account
- `signOut()`: Sign out current user
- `getCurrentUser()`: Get current logged-in user
- `getUserRole(uid)`: Get user role from Firestore
- `getAllUsers()`: Get all users (Admin only)

#### ProductRepository
- `getAllProducts()`: Fetch all products
- `getProductsByCategory(category)`: Filter by category
- `searchProducts(query)`: Search products
- `getFeaturedProduct()`: Get featured product for hero section
- `addProduct(product)`: Add product (Admin only)
- `updateProduct(productId, product)`: Update product (Admin only)

#### OrderRepository
- `createOrder(order)`: Create new order
- `getUserOrders(userId)`: Get user's orders
- `getOrderById(orderId)`: Get order details
- `updateOrderStatus(orderId, status)`: Update order status
- `getAllOrders()`: Get all orders (Admin only)

### 5. ViewModels

#### AuthViewModel
- Manages authentication state (Initial, Loading, Authenticated, Guest, Unauthenticated, Error)
- Guest mode support
- Sign in/sign up/sign out operations

#### ProductViewModel
- Manages product loading states
- Category filtering
- Search functionality
- Featured product management

#### CartViewModel
- Shopping cart management
- Add/remove items
- Update quantities
- Calculate total amount

#### OrderViewModel
- Order creation
- Order history
- Order status updates

## 🚀 Features

### Core Features
- ✅ Email/Password authentication with Firebase
- ✅ Guest mode browsing
- ✅ Product catalog with Firestore
- ✅ Category filtering
- ✅ Search functionality
- ✅ Featured product showcase ("Meal of the Day")
- ✅ Shopping cart management
- ✅ Order management
- ✅ Admin capabilities (product management, user list)

### Planned Features
- 🔄 Complete UI implementation with Jetpack Compose
- 🔄 Bottom navigation (Home, Search, Favorites, Profile)
- 🔄 Stripe payment integration
- 🔄 Favorites functionality
- 🔄 Admin dashboard

## 🔧 Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK with minimum API 31

### Firebase Configuration
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com)
2. Add an Android app with package name: `com.depi.taswear`
3. Download `google-services.json` and place it in the `app/` directory
4. Enable Firebase Authentication (Email/Password)
5. Create a Firestore database

### Firestore Collections Structure

#### users
```
{
  uid: string,
  email: string,
  role: string  // "customer" or "admin"
}
```

#### products
```
{
  sku: string,
  name: string,
  description: string,
  price: number,
  stockQty: number,
  category: string,
  imageUrl: string,
  isFeatured: boolean
}
```

#### orders
```
{
  userId: string,
  items: array,
  totalAmount: number,
  status: string,
  createdAt: timestamp
}
```

## 📝 Usage

### Initializing the AppContainer
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val appContainer = (application as TASWearApplication).appContainer
        val viewModelFactory = ViewModelFactory(appContainer)
        
        // Use viewModelFactory with ViewModelProvider
    }
}
```

### Using ViewModels
```kotlin
val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
val productViewModel: ProductViewModel = viewModel(factory = viewModelFactory)
val cartViewModel: CartViewModel = viewModel(factory = viewModelFactory)
val orderViewModel: OrderViewModel = viewModel(factory = viewModelFactory)
```

## 🛡️ Security
- Firebase Authentication for secure user management
- Firestore Security Rules should be configured to restrict admin operations
- No hardcoded credentials
- HTTPS only communications

## 📄 License
This project is part of the DEPI (Digital Egypt Pioneers Initiative) program.

## 👥 Contributors
- TAS Collection Development Team

## 📞 Support
For issues and questions, please create an issue in the repository.
