class Util {

    // Calculate straight line distance in miles between two latitudes and longitudes
    static haversineDistance(lat1, lng1, lat2, lng2) {
        let toRadians = x => x * Math.PI / 180;

        lat1 = toRadians(lat1);
        lat2 = toRadians(lat2);
        lng1 = toRadians(lng1);
        lng2 = toRadians(lng2);

        let radius = 3958.8;
        let dlat = lat2 - lat1;
        let dlng = lng2 - lng1;

        let hlat = (1 - Math.cos(dlat)) / 2;
        let hlng = (1 - Math.cos(dlng)) / 2;

        return 2 * radius * Math.asin(Math.sqrt(hlat + Math.cos(lat1) * Math.cos(lat2) * hlng));
    }

}

export default Util;
