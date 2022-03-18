import crypto from "crypto";
import { HASH_SECRET } from "../keys/secrets";

class OtpService {
  data: string;
  expiresIn: number;
  payload: string;
  otp: number;

  constructor(payload: string, otp?: number, expiresIn?: number) {
    this.expiresIn = expiresIn || Date.now() + 1000 * 60 * 2;
    this.payload = payload;
    this.otp = otp || this.generateOtp();
    this.data = `${this.payload}.${this.otp}.${this.expiresIn}`;
  }

  private generateOtp(): number {
    return crypto.randomInt(1000000,9999999)
  }

  hash() {
    return crypto
      .createHmac("sha256", HASH_SECRET)
      .update(this.data)
      .digest("hex");
  }

  static verify(email: string, otp: number, expiresIn: number, hash: string) {
    const generatedOtp = new OtpService(email, otp, expiresIn);

    return generatedOtp.hash() === hash;
  }
}

export default OtpService;