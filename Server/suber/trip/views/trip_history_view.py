from django.http import JsonResponse
from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError
from rest_framework.generics import get_object_or_404
from rest_framework.views import APIView

from account.models import Passenger, Driver
from trip.models import PassengerTripHistory, DriverTripHistory


class TripHistoryView(APIView):
    def get(self, request):
        data = request.query_params
        try:
            username = data['username']
            user_type = data['user_type']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        if user_type == 'P':
            history_model = PassengerTripHistory
            user_model = Passenger
        elif user_type == 'D':
            history_model = DriverTripHistory
            user_model = Driver
        else:
            raise ValidationError('Wrong user_type')

        user = get_object_or_404(user_model, username=username)
        history_data = list(history_model.objects.filter(
            user=user
        ).order_by('-date').values(
            'user__username',
            'source',
            'destination',
            'date'
        ))

        return JsonResponse(history_data)
