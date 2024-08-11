from django.shortcuts import render
from retailonline.models import Store, Users, Product

# Create your views here.

def index(request):
    return render(request, "index.html")

def store_overview(request):
    store_objs = Store.objects.all()
    context = {
        "stores": store_objs,
    }
    return render(request, "store_overview.html", context)

def store_detail(request, pk):
    # manager_obj = Users.objects.get(pk=pk)
    # store_objs = Store.objects.filter(managerid=manager_obj.userid)
    store_obj = Store.objects.get(pk=pk)
    context = {
        "store": store_obj,
    }
    return render(request, "store_detail.html", context)

def user_detail(request, pk):
    user_obj = Users.objects.get(pk=pk)
    context = {
        "user": user_obj,
    }
    return render(request, "user_detail.html", context)

def store_products(request, pk):
    store_obj = Store.objects.get(pk=pk)
    product_objs = Product.objects.filter(storeid=store_obj.storeid)
    context = {
        "store": store_obj,
        "products": product_objs,
    }
    return render(request, "store_products.html", context)