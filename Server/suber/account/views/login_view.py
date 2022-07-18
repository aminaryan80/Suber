from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Passenger, Driver


class LoginView(APIView):

    def post(self, request):
        data = request.data
        try:
            username = data['username']
            password = data['password']
            user_type = data['user_type']
        except MultiValueDictKeyError:
            raise ValidationError('Username or Password is missing!')

        self._login(username, password, user_type)

        return Response('ok')

    def _login(self, username, password, user_type):
        if user_type == 'P':
            self._login_passenger(username, password)
        elif user_type == 'D':
            self._login_driver(username, password)
        else:
            raise ValidationError('Wrong user_type!')

    def _login_passenger(self, username, password):
        try:
            user = Passenger.objects.get(username=username)
            assert user.password == password
        except Exception:
            raise ValidationError('Wrong username or password!')

    def _login_driver(self, username, password):
        try:
            user = Driver.objects.get(username=username)
            assert user.password == password
        except Exception:
            raise ValidationError('Wrong username or password!')
