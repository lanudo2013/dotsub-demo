export class LogService {

    constructor() {
    }

    public logError(error: Error, priority: string) {
        console.error(error);
    }

}

export default new LogService();
