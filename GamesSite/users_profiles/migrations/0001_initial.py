# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import datetime
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='UserProfile',
            fields=[
                ('id', models.AutoField(serialize=False, primary_key=True, auto_created=True, verbose_name='ID')),
                ('category', models.CharField(max_length=20, choices=[('Developer', 'Developer'), ('Gamer', 'Gamer')])),
                ('receive_news', models.BooleanField(default=True)),
                ('picture', models.ImageField(upload_to='profile_images', blank=True)),
                ('name', models.CharField(max_length=10)),
                ('created_on', models.DateTimeField(default=datetime.datetime.now, auto_now_add=True)),
                ('updated_on', models.DateTimeField(default=datetime.datetime.now, auto_now=True)),
                ('user', models.OneToOneField(to=settings.AUTH_USER_MODEL)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
