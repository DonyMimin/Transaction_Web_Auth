## How To Start

### Alur Autentikasi
1. User sends login request with username/password
2. `AuthenticationManager` verifies credentials
3. If valid, creates JWT token
4. Token is sent back to client
5. Client includes token in Authorization header for subsequent requests

### BY HIT API
1. Login:
http
POST /api/auth/login
Content-Type: application/json

{
    "username": "..",
    "password": ".."
}

2. Get the token Auth and apply it on other API's