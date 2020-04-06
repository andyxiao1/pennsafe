const { MongoClient } = require("mongodb");
const uri = "mongodb+srv://skyler:skyler@safteyappcluster-ymsv8.mongodb.net/test?retryWrites=true&w=majority";

class DBHandler {

    constructor(dbName, collectionName) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.client = new MongoClient(uri, {useUnifiedTopology: true});
    }

    async init() {
        try {
            await this.client.connect();
            this.db = this.client.db(this.dbName);
            console.log(`DBHandler connected to database ${this.dbName}.`);
        }
        catch (e) { console.log(e); }
    }

    findRecord(data, callback) {
        const collection = this.db.collection(this.collectionName);
        collection.find(data).toArray(callback);
    }

    addRecord(record, callback) {
        const collection = this.db.collection(this.collectionName);
        collection.insertOne(record, callback);
    }

    deleteMatchingRecord(data, callback) {
        const collection = this.db.collection(this.collectionName);
        collection.deleteOne(data, callback);
    }

}

module.exports = DBHandler;