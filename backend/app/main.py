from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from .database.db import Base, engine
from .routes import auth, routes_api, activity_api

Base.metadata.create_all(bind=engine)

app = FastAPI(title="TrailMate API")

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routers with API prefix
app.include_router(auth.router, prefix="/api/auth")
app.include_router(routes_api.router, prefix="/api")
app.include_router(activity_api.router, prefix="/api")

@app.get("/")
def read_root():
    return {"message": "TrailMate API is running!"}

@app.on_event("startup")
def startup():
    Base.metadata.create_all(bind=engine)

