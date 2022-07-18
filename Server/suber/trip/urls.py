from django.urls import path

from trip.views.trip_history_view import TripHistoryView

urlpatterns = [
    path('trip-history/', TripHistoryView.as_view(), name='trip-history'),
]
