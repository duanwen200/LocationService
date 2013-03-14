// mapArea.js
// by vampirefan
// mapArea Model: save, findOne, findAll in mongodb
// -----------------------------------------------------
var mongodb = require('./db');

function MapArea(maparea) {
  this.mapAreaId = maparea.mapAreaId; //唯一，可以由经纬度生成
  this.mapAreaInfo = maparea.mapAreaInfo; //地理位置信息（国家、城市、街道、楼、层、房间号）
  this.longitudeLD = maparea.longitudeLD; //地图左下角的经度
  this.latitudeLD = maparea.latitudeLD; //地图左下角的纬度
  this.longitudeRU = maparea.longitudeRU; //地图右上角的经度
  this.latitudeRU = maparea.latitudeRU; //地图右上角的纬度
  this.altitude = maparea.altitude; //地图所在海拔
  this.accuracy = maparea.accuracy; //精确程度
}

module.exports = MapArea;

MapArea.prototype.insert = function insert(callback) {
  // insert to mongodb
  var maparea = {
    mapAreaId: this.mapAreaId,
    mapAreaInfo: this.mapAreaInfo,
    longitudeLD: this.longitudeLD,
    latitudeLD: this.latitudeLD,
    longitudeRU: this.longitudeRU,
    latitudeRU: this.latitudeRU,
    altitude: this.altitude,
    accuracy: this.accuracy
  };
  mongodb.open(function(err, db) {
    if(err) {
      return callback(err);
    }
    // find fingerprints collection
    db.collection('mapareas', function(err, collection) {
      if(err) {
        mongodb.close();
        return callback(err);
      }
      // add index for mapAreaId
      collection.ensureIndex('mapAreaId', {
        unique: true
      });
      // insert maparea into colletion
      collection.insert(maparea, {
        safe: true
      }, function(err, maparea) {
        mongodb.close();
        callback(err, maparea);
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