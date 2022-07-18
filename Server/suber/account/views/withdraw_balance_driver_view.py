from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Driver, Transaction


class WithdrawBalanceDriverView(APIView):
    def post(self, request):
        data = request.data
        try:
            username = data['username']
            card_number = data['card_number']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        self._withdraw_balance(username, card_number)

        return Response('ok')

    def _withdraw_balance(self, username, card_number):
        driver = get_object_or_404(Driver, username=username)

        Transaction.objects.create(
            driver=driver,
            card_number=card_number,
            amount=driver.balance
        )

        driver.balance = 0
        driver.save()
