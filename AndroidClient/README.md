Location Client
------------------------
by BANG, vampirefan


# The Client:
1. start the Client.
2. Set the Server's host IP Address and port.

NOTE: check the example fingerFrame and locateFrame in `./db/`

## the postData fingerfraem (JSON)

        {
          "locationId": {
            "buildingId": "E4",
            "floorId": "1",
            "roomId": "102",
            "locationX": "1",
            "locationY": "1"
          },
          "bearing": 0,
          "wapInfo": [{
            "bssid": "00:60:2f:3a:07:65",
            "rssi": -69
          }, {
            "bssid": "00:60:2f:3a:07:15",
            "rssi": -64
          }, {
            "bssid": "00:60:2f:3a:07:f5",
            "rssi": -25
          }, {
            "bssid": "00:60:2f:3a:07:b5",
            "rssi": -67
          }]
        }

## the postData locateframe (JSON)

        # bearing:
        (罗盘显示的)方位

        # wapInfo
        各个wap的信号强度值
        ## bssid
        wap的bssid值
        ## rssi
        wap的信号强度值
        example: 
            {
                "bearing": 0,
                "wapInfo": [{
                    "bssid": "00:60:2f:3a:07:35",
                    "rssi": 45
                }, {
                    "bssid": "00:60:2f:3a:07:65",
                    "rssi": 56
                }, {
                    "bssid": "00:60:2f:3a:07:15",
                    "rssi": 67
                }, {
                    "bssid": "00:60:2f:3a:07:f5",
                    "rssi": 78
                }, {
                    "bssid": "00:60:2f:3a:07:b5",
                    "rssi": 89
                }]
            }


# Function:
use url parse and http post to get the system work    

example:

+ `/start`, `/`: test connection;
+ `/login`: get the instruction;
+ `/finger`: post current locationId and wap_rssis;
+ `/locate`: post current wap_rssis and get current locationId;
+ `/dbshow`: get a instance glance of the fingerprints database;
+ `/LocationServerTest.fingerprints.remove()`: remove all fingerprints data;