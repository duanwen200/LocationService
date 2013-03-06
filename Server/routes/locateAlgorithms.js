// locateAlgorithms.js
// by vampirefan
// Algorithms for positioning. include Euclidean Distance Algorithm(ed),
// -----------------------------------------------------
var Fingerprint = require("../models/fingerprint");

// Euclidean Distance Algorithm

function ed_origin(body, locateFrame, callback) {
  console.log("The Origin Euclidean Distance Algorithm was called.");

  Fingerprint.getAll(function(err, fingerprints) {
    if(err) {
      return callback(err);
    }
    var minDistance = 65535;
    var minDistanceIndex = 0;
    var fingersdistantce = new Array(fingerprints.length);
    for(var i = 0; i < fingerprints.length; ++i) {
      var tempdistance = 0;
      for(var j = 0; j < fingerprints[i].wapInfo.length; ++j) {
        tempdistance += (fingerprints[i].wapInfo[j].rssi - locateFrame.wapInfo[j].rssi) * (fingerprints[i].wapInfo[j].rssi - locateFrame.wapInfo[j].rssi);
      }
      fingersdistantce[i] = tempdistance;
      if(fingersdistantce[i] < minDistance) {
        minDistance = fingersdistantce[i];
        minDistanceIndex = i;
      }
    }

    if(isNaN(fingersdistantce[0])) {
      console.log('Calculation Failed. Invalid postData.');
      body += 'Calculation Failed. Invalid postData.\n';
    } else {
      var locationId = fingerprints[minDistanceIndex].locationId;
      console.log('Calculation done. Returned locationId: ' + locationId);
      body += 'Calculation done. You get your current location: ' + 'locationId=' + locationId.toString() + ';\n';
    }
    return callback(err, body);
  });
}

function ed_advanced(body, locateFrame, callback) {
  console.log("The Advanced Euclidean Distance Algorithm was called.");

  var wapNumber = locateFrame.wapInfo.length;
  var wapBssids = new Array(wapNumber);
  for(var q = 0; q < wapNumber; ++q) {
    wapBssids[q] = locateFrame.wapInfo[q].bssid;
  }
  // console.log(wapNumber);
  // console.log(wapBssids);
  Fingerprint.getByWapBssids(wapBssids, function(err, fingerprints) {
    if(err) {
      return callback(err);
    }
    var minDistance = 65535;
    var minDistanceIndex = 0;

    var fingersdistantce = new Array(fingerprints.length);
    for(var i = 0; i < fingerprints.length; ++i) {
      var tempdistance = 0;
      var tempRssi = new Array(wapNumber);

      for(var j = 0; j < wapNumber; ++j) {
        tempRssi[j] = -120;
        for(var k = 0; k < fingerprints[i].wapInfo.length; ++k) {
          if(fingerprints[i].wapInfo[k].bssid === wapBssids[j]) tempRssi[j] = fingerprints[i].wapInfo[k].rssi;
        }
        tempdistance += (tempRssi[j] - locateFrame.wapInfo[j].rssi) * (tempRssi[j] - locateFrame.wapInfo[j].rssi);
      }
      fingersdistantce[i] = tempdistance;
      if(fingersdistantce[i] < minDistance) {
        minDistance = fingersdistantce[i];
        minDistanceIndex = i;
      }
      // console.log(tempRssi);
      // console.log(fingersdistantce);
    }

    if(isNaN(fingersdistantce[0])) {
      console.log('Calculation Failed. Invalid postData.');
      body += 'Calculation Failed. Invalid postData.\n';
    } else {
      var locationId = fingerprints[minDistanceIndex].locationId;
      console.log('Calculation done. Returned locationId: ' + locationId);
      body += 'Calculation done. You get your current location: ' + 'locationId=' + locationId.toString() + ';\n';
    }
    return callback(err, body);
  });
}

exports.ed_origin = ed_origin;
exports.ed_advanced = ed_advanced;