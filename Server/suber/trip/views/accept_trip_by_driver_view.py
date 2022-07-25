from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Passenger, Driver
from trip.models import OnGoingTrip, DriverTripHistory, PassengerTripHistory


class AcceptTripByDriverView(APIView):
    def post(self, request):
        data = request.data
        try:
            username = data['username']
            trip_id = data['trip_id']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        self._accept_trip(username, trip_id)

        return Response('ok')

    def _accept_trip(self, username, trip_id):
        driver = get_object_or_404(Driver, username=username)
        trip = OnGoingTrip.objects.get(pk=trip_id)
        passenger = trip.passenger

        PassengerTripHistory.objects.create(
            passenger=passenger,
            driver=driver,
            source=trip.source,
            destination=trip.destination,
            price=trip.price
        )
        DriverTripHistory.objects.create(
            driver=driver,
            source=trip.source,
            destination=trip.destination,
            price=trip.price
        )

        passenger.balance -= trip.price
        driver.balance += trip.price
        passenger.save()
        driver.save()

        OnGoingTrip.objects.get(pk=trip_id).delete()
