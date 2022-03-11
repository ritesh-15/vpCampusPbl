import { config } from "dotenv";

config();

export const ACESS_TOKEN_SECRET = <string>process.env.ACCESS_TOKEN_SECRET;

export const REFRESH_TOKEN_SECRET = <string>process.env.REFRESH_TOKEN_SECRET;

export const MONGO_URI = <string>process.env.MONGO_URI;

export const CLOUD_NAME = <string>process.env.CLOUD_NAME;

export const CLOUDINARY_API_KEY = <string>process.env.CLOUDINARY_API_KEY;

export const CLOUDINARY_API_SECRET = <string>process.env.CLOUDINARY_API_SECRET;

export const SEND_GRID_API_KEY = <string>process.env.SEND_GRID_API_KEY

export const SEND_GRID_EMAIL = <string>process.env.SEND_GRID_EMAIL

export const HASH_SECRET = <string> process.env.HASH_SECRET