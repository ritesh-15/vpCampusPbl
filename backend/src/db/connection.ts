import mongoose from "mongoose";
import { MONGO_URI } from "../keys/secrets";

export const connection = async () => {
  try {
    await mongoose.connect(MONGO_URI);
    console.log("Database connected!");
  } catch (error: any) {
    console.log(error);
  }
};
