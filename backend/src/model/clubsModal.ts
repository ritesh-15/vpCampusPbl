import { Schema, model } from "mongoose";
import { ClubInterface } from "../interfaces/ClubInterface";

const clubSchema = new Schema<ClubInterface>(
  {
    name: {
      type: String,
      required: true,
      unique: true,
    },
    description: {
      type: String,
      required: true,
    },
    admin: {
      type: Schema.Types.ObjectId,
      ref: "users",
      required: true,
    },
    avatar: {
      url: {
        type: String,
        default: "",
      },
      publicId: {
        type: String,
        default: "",
      },
    },
  },
  { timestamps: true }
);

const Clubs = model("clubs", clubSchema);

export default Clubs;
