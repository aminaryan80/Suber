from django.http import JsonResponse
from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError, NotFound
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Passenger
from trip.models import OnGoingTrip, PassengerTripHistory


class GetOngoingTripStatusView(APIView):
    def get(self, request):
        data = request.query_params
        try:
            username = data['username']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        passenger = get_object_or_404(Passenger, username=username)

        if OnGoingTrip.objects.filter(passenger=passenger).count() > 0:
            return Response('searching')

        driver = PassengerTripHistory.objects.filter(passenger=passenger).last().driver

        if driver:
            return JsonResponse({'name': f'{driver.first_name} {driver.last_name}', 'car': driver.car})

        raise NotFound('trip not found!')
