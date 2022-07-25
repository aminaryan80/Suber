from django.contrib import admin

# Register your models here.
from trip.models import PassengerTripHistory, DriverTripHistory, OnGoingTrip

admin.site.register(PassengerTripHistory)
admin.site.register(DriverTripHistory)
admin.site.register(OnGoingTrip)
