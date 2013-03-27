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
    // find mapareas collection
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

MapArea.getOne = function getOne(getMapAreaId, callback) {
  mongodb.open(function(err, db) {
    if(err) {
      return callback(err);
    }
    // find mapareas collection
    db.collection('mapareas', function(err, collection) {
      if(err) {
        mongodb.close();
        return callback(err);
      }
      // find docs which mapAreaId = getMapAreaId
      collection.findOne({
        mapAreaId: getMapAreaId
      }, function(err, doc) {
        mongodb.close();
        if(doc) {
          // Object MapArea
          var maparea = new MapArea(doc);
          return callback(err, maparea);
        } else {
          return callback(err, null);
        }
      });
    });
  });
};
