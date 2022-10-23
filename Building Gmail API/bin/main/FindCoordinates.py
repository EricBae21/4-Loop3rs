from geopy.geocoders import Nominatim

file = open("addresses.txt", 'r')
coordinates = []

for address in file:
    geolocator = Nominatim(user_agent="UW Alert")
    location = geolocator.geocode(address)
    add = str(location.latitude) + " " + str(location.longitude)
    coordinates.append(add)

file.close()

file_location = open("coordinates.txt", "w")
file_location.writelines("% s\n" % c for c in coordinates)

file_location.close()