from django.http import JsonResponse
from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError
from rest_framework.generics import get_object_or_404
from rest_framework.views import APIView

from account.models import Passenger, Driver


class GetProfileView(APIView):

    def get(self, request):
        data = request.query_params
        try:
            username = data['username']
            user_type = data['user_type']
        except MultiValueDictKeyError:
            raise ValidationError('Username or Password is missing!')

        if user_type == 'P':
            user_model = Passenger
        elif user_type == 'D':
            user_model = Driver
        else:
            raise ValidationError('Wrong user_type')

        user = get_object_or_404(user_model, username=username)

        return JsonResponse(
            {
                'username': user.username,
                'first_name': user.first_name,
                'last_name': user.last_name,
                'balance': user.balance,
                'extra': user.car if user_type == 'D' else ''
            }
        )
