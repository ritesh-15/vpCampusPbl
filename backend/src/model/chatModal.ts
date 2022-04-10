import mongoose from "mongoose";
import { Schema } from "mongoose";
import { ChatInterface } from "../interfaces/ChatInterface";

const chatSchema = new Schema<ChatInterface>(
  {
    clubId: {
      type: Schema.Types.ObjectId,
      required: true,
      ref: "clubs",
    },
    userId: {
      type: Schema.Types.ObjectId,
      required: true,
      ref: "users",
    },
    message: {
      type: String,
      required: true,
    },
  },
  { timestamps: true }
);

const Chat = mongoose.model("chats", chatSchema);

export default Chat;
