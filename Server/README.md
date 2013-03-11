Location Server 
-----------------------------
by vampirefan

NOTE: Using mongodb to store and frame fingers.

# The Server:
1. install nodejs.
2. install and setup mongodb.
3. into the dictory: `cd $(locationServerDemo)`
4. start the server: `node app.js`

## The finger DataBase (JSON)

        fingerPrints:
        # locationId: 

        # bearing:
        (罗盘显示的)方位

        # wapInfo
        各个wap的信号强度值
        ## bssid
        wap的bssid值
        ## rssi
        wap的信号强度值

        # options:可选数据
        ## wapNumScan:
        扫描到wap的个数

        example:
        [{
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
        }, {
            "locationId": {
                "buildingId": "E4",
                "floorId": "1",
                "roomId": "102",
                "locationX": "1",
                "locationY": "2"
            },
            "bearing": 0,
            "wapInfo": [{
                "bssid": "00:60:2f:3a:07:35",
                "rssi": -45
            }, {
                "bssid": "00:60:2f:3a:07:65",
                "rssi": -56
            }, {
                "bssid": "00:60:2f:3a:07:15",
                "rssi": -67
            }, {
                "bssid": "00:60:2f:3a:07:f5",
                "rssi": -78
            }, {
                "bssid": "00:60:2f:3a:07:b5",
                "rssi": -89
            }]
        }]


