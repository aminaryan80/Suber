from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError, NotAcceptable
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Passenger
from trip.models import OnGoingTrip


class HurryUpTripView(APIView):

    def put(self, request):
        data = request.data
        try:
            username = data['username']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        self._hurry_up_trip(username)

        return Response('ok')

    def _hurry_up_trip(self, username):
        passenger = get_object_or_404(Passenger, username=username)

        trip = OnGoingTrip.objects.filter(passenger=passenger).first()
        price = trip.price

        if not trip:
            raise NotAcceptable('You have no on-going trip!')

        if OnGoingTrip.objects.filter(passenger=passenger).count() > 1:
            OnGoingTrip.objects.filter(pk__neq=trip.id, passenger=passenger).delete()

        if passenger.balance < price * 1.2:
            raise NotAcceptable('Your balance is lower than trip price!')

        trip.price = price * 1.2
        trip.save()
