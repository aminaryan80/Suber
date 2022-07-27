from django.http import JsonResponse
from rest_framework.views import APIView

from discount.models import DiscountCode


class GetDiscountCodesView(APIView):
    def get(self, request):
        data = list(DiscountCode.objects.values(
            'code',
            'percent'
        ))
        return JsonResponse(data, safe=False)
