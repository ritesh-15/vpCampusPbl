import { config } from "dotenv";

config();

export const ACESS_TOKEN_SECRET = <string>process.env.ACCESS_TOKEN_SECRET;

export const REFRESH_TOKEN_SECRET = <string>process.env.REFRESH_TOKEN_SECRET;

export const MONGO_URI = <string>process.env.MONGO_URI;

export const CLOUD_NAME = <string>process.env.CLOUD_NAME;

export const CLOUDINARY_API_KEY = <string>process.env.CLOUDINARY_API_KEY;

export const CLOUDINARY_API_SECRET = <string>process.env.CLOUDINARY_API_SECRET;
