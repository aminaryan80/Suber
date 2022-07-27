from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError, NotAcceptable
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Passenger


class ChargeBalancePassengerView(APIView):
    def post(self, request):
        data = request.data
        try:
            username = data['username']
            amount = data['amount']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        self._charge_balance(username, amount)

        return Response('ok')

    def _charge_balance(self, username, amount):
        if not self._verify_payment():
            raise NotAcceptable('Transaction was not successful!')

        passenger = get_object_or_404(Passenger, username=username)
        passenger.balance += int(amount)
        passenger.save()

    def _verify_payment(self):
        return True
