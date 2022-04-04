import { EventEmitter } from "stream";

class EmitterInstance {
  private static emiiter: EventEmitter | null = null;

  static getInstance(): EventEmitter {
    if (this.emiiter == null) {
      this.emiiter = new EventEmitter();
      return this.emiiter;
    }
    return this.emiiter;
  }
}

export default EmitterInstance;
