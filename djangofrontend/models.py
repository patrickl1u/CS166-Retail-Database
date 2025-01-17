# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey and OneToOneField has `on_delete` set to the desired behavior
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from django.db import models


class AuthGroup(models.Model):
    name = models.CharField(unique=True, max_length=150)

    class Meta:
        managed = False
        db_table = 'auth_group'


class AuthGroupPermissions(models.Model):
    id = models.BigAutoField(primary_key=True)
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)
    permission = models.ForeignKey('AuthPermission', models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_group_permissions'
        unique_together = (('group', 'permission'),)


class AuthPermission(models.Model):
    name = models.CharField(max_length=255)
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING)
    codename = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'auth_permission'
        unique_together = (('content_type', 'codename'),)


class AuthUser(models.Model):
    password = models.CharField(max_length=128)
    last_login = models.DateTimeField(blank=True, null=True)
    is_superuser = models.BooleanField()
    username = models.CharField(unique=True, max_length=150)
    first_name = models.CharField(max_length=150)
    last_name = models.CharField(max_length=150)
    email = models.CharField(max_length=254)
    is_staff = models.BooleanField()
    is_active = models.BooleanField()
    date_joined = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'auth_user'


class AuthUserGroups(models.Model):
    id = models.BigAutoField(primary_key=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_groups'
        unique_together = (('user', 'group'),)


class AuthUserUserPermissions(models.Model):
    id = models.BigAutoField(primary_key=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    permission = models.ForeignKey(AuthPermission, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_user_permissions'
        unique_together = (('user', 'permission'),)


class DjangoAdminLog(models.Model):
    action_time = models.DateTimeField()
    object_id = models.TextField(blank=True, null=True)
    object_repr = models.CharField(max_length=200)
    action_flag = models.SmallIntegerField()
    change_message = models.TextField()
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING, blank=True, null=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'django_admin_log'


class DjangoContentType(models.Model):
    app_label = models.CharField(max_length=100)
    model = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'django_content_type'
        unique_together = (('app_label', 'model'),)


class DjangoMigrations(models.Model):
    id = models.BigAutoField(primary_key=True)
    app = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    applied = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_migrations'


class DjangoSession(models.Model):
    session_key = models.CharField(primary_key=True, max_length=40)
    session_data = models.TextField()
    expire_date = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_session'


class Orders(models.Model):
    ordernumber = models.AutoField(primary_key=True)
    customerid = models.ForeignKey('Users', models.DO_NOTHING, db_column='customerid')
    storeid = models.ForeignKey('Product', models.DO_NOTHING, db_column='storeid')
    productname = models.CharField(max_length=30)
    unitsordered = models.IntegerField()
    ordertime = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'orders'


class Product(models.Model):
    storeid = models.OneToOneField('Store', models.DO_NOTHING, db_column='storeid', primary_key=True)
    productname = models.CharField(max_length=30)
    numberofunits = models.IntegerField()
    priceperunit = models.FloatField()

    class Meta:
        managed = False
        db_table = 'product'
        unique_together = (('storeid', 'productname'),)


class Productsupplyrequests(models.Model):
    requestnumber = models.AutoField(primary_key=True)
    managerid = models.ForeignKey('Users', models.DO_NOTHING, db_column='managerid')
    warehouseid = models.ForeignKey('Warehouse', models.DO_NOTHING, db_column='warehouseid')
    storeid = models.ForeignKey(Product, models.DO_NOTHING, db_column='storeid')
    productname = models.CharField(max_length=30)
    unitsrequested = models.IntegerField()

    class Meta:
        managed = False
        db_table = 'productsupplyrequests'


class Productupdates(models.Model):
    updatenumber = models.AutoField(primary_key=True)
    managerid = models.ForeignKey('Users', models.DO_NOTHING, db_column='managerid')
    storeid = models.ForeignKey(Product, models.DO_NOTHING, db_column='storeid')
    productname = models.CharField(max_length=30)
    updatedon = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'productupdates'


class Store(models.Model):
    storeid = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=30)
    latitude = models.DecimalField(max_digits=8, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)
    managerid = models.ForeignKey('Users', models.DO_NOTHING, db_column='managerid')
    dateestablished = models.DateField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'store'


class Users(models.Model):
    userid = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50)
    password = models.CharField(max_length=11)
    latitude = models.DecimalField(max_digits=8, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)
    type = models.CharField(max_length=10)

    class Meta:
        managed = False
        db_table = 'users'


class Warehouse(models.Model):
    warehouseid = models.IntegerField(primary_key=True)
    area = models.IntegerField(blank=True, null=True)
    latitude = models.DecimalField(max_digits=8, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)

    class Meta:
        managed = False
        db_table = 'warehouse'
