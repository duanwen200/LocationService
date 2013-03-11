// fingerprint.js
// by vampirefan
// Fingerprint Model: save, findOne, findAll in mongodb
// -----------------------------------------------------
var mongodb = require('./db');

function Fingerprint(fingerprint) {
  this.locationId = fingerprint.locationId;
  this.bearing = fingerprint.bearing;
  this.wapInfo = fingerprint.wapInfo;
}

module.exports = Fingerprint;

Fingerprint.prototype.insert = function insert(callback) {
  // insert to mongodb
  var fingerprint = {
    locationId: this.locationId, //唯一，可以由经纬度生成
    locationInfo: this.locationInfo, //地理位置信息（国家、城市、街道、楼、层、房间号）
    longitude: this.longitude, //经度
    latitude: this.latitude, //纬度
    accuracy: this.accuracy, //精确程度
    bearing: this.bearing, //方向
    wapInfo: this.wapInfo //扫描得到的WAP信息，
  };
  mongodb.open(function(err, db) {
    if(err) {
      return callback(err);
    }
    // find fingerprints collection
    db.collection('fingerprints', function(err, collection) {
      if(err) {
        mongodb.close();
        return callback(err);
      }
      // add index for locationId
      collection.ensureIndex('locationId', {
        unique: true
      });
      // insert fingerprint into colletion
      collection.insert(fingerprint, {
        safe: true
      }, function(err, fingerprint) {
        mongodb.close();
        callback(err, fingerprint);
      });
    });
  });
};

Fingerprint.getOne = function getOne(getLocationId, callback) {
  mongodb.open(function(err, db) {
    if(err) {
      return callback(err);
    }
    // find fingerprints collection
    db.collection('fingerprints', function(err, collection) {
      if(err) {
        mongodb.close();
        return callback(err);
      }
      // find docs which locationId = getLocationId
      collection.findOne({
        locationId: getLocationId
      }, function(err, doc) {
        mongodb.close();
        if(doc) {
          // Object Fingerprint
          var fingerprint = new Fingerprint(doc);
          return callback(err, fingerprint);
        } else {
          return callback(err, null);
        }
      });
    });
  });
};

Fingerprint.getByWapBssids = function getByWapBssids(wapBssids, callback) {
  mongodb.open(function(err, db) {
    if(err) {
      return callback(err);
    }
    // find fingerprint collection
    db.collection('fingerprints', function(err, collection) {
      if(err) {
        mongodb.close();
        return callback(err);
      }
      // find docs which WapInfo.bssid $in getWapInfo.bssids
      collection.find({
        "wapInfo.bssid": {
          "$in": wapBssids
        }
      }).toArray(function(err, docs) {
        mongodb.close();
        if(docs) {
          // Object Fingerprint
          var fingerprints = docs;
          return callback(err, fingerprints);
        } else {
          return callback(err, null);
        }
      });
    });
  });
};

Fingerprint.getAll = function getAll(callback) {
  mongodb.open(function(err, db) {
    if(err) {
      return callback(err);
    }
    // find fingerprint collection
    db.collection('fingerprints', function(err, collection) {
      if(err) {
        mongodb.close();
        return callback(err);
      }
      // find docs which locationId = fingerprint.locationId
      collection.find().toArray(function(err, docs) {
        mongodb.close();
        if(docs) {
          // Object Fingerprint
          var fingerprints = docs;
          return callback(err, fingerprints);
        } else {
          return callback(err, null);
        }
      });
    });
  });
};


Fingerprint.removeAll = function removeAll(callback) {
  mongodb.open(function(err, db) {
    if(err) {
      return callback(err);
    }
    // find fingerprint collection
    db.collection('fingerprints', function(err, collection) {
      if(err) {
        mongodb.close();
        return callback(err);
      }
      // remove all fingerprints
      collection.remove();
      mongodb.close();
      return callback(err);
    });
  });
};

Fingerprint.removeByLocationId = function removeByLocationId(getLocationId, callback) {
  mongodb.open(function(err, db) {
    if(err) {
      return callback(err);
    }
    // find fingerprints collection
    db.collection('fingerprints', function(err, collection) {
      if(err) {
        mongodb.close();
        return callback(err);
      }
      // find docs which locationId = getLocationId
      collection.remove({
        locationId: getLocationId
      }, function(err) {
        mongodb.close();
      return callback(err);
      });
    });
  });
};