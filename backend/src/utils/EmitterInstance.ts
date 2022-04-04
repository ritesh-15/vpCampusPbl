import { EventEmitter } from "stream";

class EmitterInstance {
  private static emiiter: EventEmitter | null = null;

  static getInstance(): EventEmitter {
    if (this.emiiter == null) {
      return new EventEmitter();
    }
    return this.emiiter;
  }

  static setInstance() {
    if (this.emiiter == null) {
      this.emiiter = new EventEmitter();
    }
  }
}

export default EmitterInstance;
