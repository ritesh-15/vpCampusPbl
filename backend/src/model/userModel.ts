import { Schema, model } from "mongoose";
import { USER } from "../constants/userRoles";
import { UserInterface } from "../interfaces/UserInterface";

const userSchema = new Schema<UserInterface>(
  {
    name: {
      type: String,
      required: true,
    },
    password: {
      type: String,
      required: true,
      select: false,
    },
    avatar: {
      publicId: {
        type: String,
        default: "",
      },
      url: {
        type: String,
        default: "",
      },
    },
    department: {
      type: String,
      default: "",
    },
    yearOfStudy: {
      type: String,
      default: "",
    },
    bio: {
      type: String,
      default: "",
    },
    email: {
      type: String,
      required: true,
      unique: true,
    },
    isActivated: {
      type: Boolean,
      default: false,
    },
    role: {
      type: String,
      default: USER,
    },
  },
  {
    timestamps: true,
  }
);

const User = model<UserInterface>("users", userSchema);

export default User;
