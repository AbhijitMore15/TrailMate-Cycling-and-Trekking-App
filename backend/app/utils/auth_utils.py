from datetime import datetime, timedelta
from passlib.context import CryptContext
from jose import JWTError, jwt

# ===============================
# Password Hashing (Argon2)
# ===============================
pwd_context = CryptContext(
    schemes=["argon2"],
    deprecated="auto"
)

# ===============================
# JWT Configuration
# ===============================
SECRET_KEY = "your-secret-key-change-this-in-production"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30


def hash_password(password: str) -> str:
    """
    Hash a password securely using Argon2
    """
    return pwd_context.hash(password)


def verify_password(plain_password: str, hashed_password: str) -> bool:
    """
    Verify a password against stored hash
    """
    return pwd_context.verify(plain_password, hashed_password)


def create_access_token(data: dict, expires_delta: timedelta | None = None) -> str:
    """
    Create JWT access token
    """
    to_encode = data.copy()

    expire = datetime.utcnow() + (
        expires_delta
        if expires_delta
        else timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    )

    to_encode.update({"exp": expire})
    return jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)


def verify_token(token: str) -> int | None:
    """
    Verify JWT token and extract user ID
    """
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        user_id = payload.get("sub")
        return int(user_id) if user_id else None
    except JWTError:
        return None


def get_token_from_header(authorization: str) -> str | None:
    """
    Extract token from Authorization header
    Format: Bearer <token>
    """
    if not authorization or not authorization.startswith("Bearer "):
        return None
    return authorization.split(" ")[1]
