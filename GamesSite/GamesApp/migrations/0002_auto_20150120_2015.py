# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('GamesApp', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='players',
            name='created_on',
        ),
        migrations.RemoveField(
            model_name='players',
            name='updated_on',
        ),
    ]
