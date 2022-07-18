from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Passenger, Driver


class RegisterView(APIView):

    def post(self, request):
        data = request.data
        try:
            username = data['username']
            password = data['password']
            first_name = data['first_name']
            last_name = data['last_name']
            user_type = data['user_type']
            car = data['extra']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        self._register(username, password, first_name, last_name, car, user_type)

        return Response('ok')

    def _register(self, username, password, first_name, last_name, car, user_type):
        if user_type == 'P':
            self._register_passenger(username, password, first_name, last_name)
        elif user_type == 'D':
            self._register_driver(username, password, first_name, last_name, car)
        else:
            raise ValidationError('Wrong user_type!')

    def _register_passenger(self, username, password, first_name, last_name):
        try:
            count = Passenger.objects.filter(username=username).count()
            assert count == 0

            Passenger.objects.create(
                username=username,
                password=password,
                first_name=first_name,
                last_name=last_name
            )

        except Exception:
            raise ValidationError('Username already exists!')

    def _register_driver(self, username, password, first_name, last_name, car):
        try:
            count = Driver.objects.filter(username=username).count()
            assert count == 0

            Driver.objects.create(
                username=username,
                password=password,
                first_name=first_name,
                last_name=last_name,
                car=car
            )

        except Exception:
            raise ValidationError('Username already exists!')
