# Unisoc Bootloader Checker (Android APK)

**⚠️ ВНИМАНИЕ: ИСПОЛЬЗОВАНИЕ ДАННОГО ПРИЛОЖЕНИЯ И ЛЮБЫХ МЕТОДОВ РАЗБЛОКИРОВКИ ЗАГРУЗЧИКА СОПРЯЖЕНО С ВЫСОКИМ РИСКОМ ПОЛОМКИ УСТРОЙСТВА (КИРПИЧ) И ПОТЕРИ ГАРАНТИИ. ВЫ ДЕЛАЕТЕ ВСЁ НА СВОЙ СТРАХ И РИСК.**

## Назначение

Это Android-приложение проверяет, возможно ли на вашем устройстве с процессором Unisoc (Spreadtrum) использовать уязвимость CVE-2022-38694 для разблокировки загрузчика

Приложение:
*   **НЕ ВЫПОЛНЯЕТ** саму разблокировку.
*   **ОЦЕНИВАЕТ РИСКИ** на основе анализа системной информации.
*   **ТРЕБУЕТ ROOT-ДОСТУПА** для глубокой проверки системы.

## Поддерживаемые проверки

1.  **Наличие root-прав** (обязательно)
2.  **Производитель SoC**: Определение, что устройство работает на Unisoc/Spreadtrum.
3.  **Уровень патча безопасности**: Сравнение с датой фикса уязвимости (ориентировочно Июнь 2025)
4.  **Версия ядра**: Анализ версии ядра Linux.
5.  **Признаки Anti-Rollback**: Попытка обнаружения механизма, блокирующего откат прошивки
   
## Как использовать

1.  Установите APK на устройство **с полученными root-правами** (например, через Magisk).
2.  Запустите приложение.
3.  Нажмите кнопку "Проверить устройство".
4.  **Предоставьте запрашиваемые root-права**.
5.  Ознакомьтесь с результатом и **ВНИМАТЕЛЬНО** с предупреждениями.

## Интерпретация результатов

*   `✅ ВЕРЯТНОСТЬ УСПЕХА ВЫСОКАЯ`: Уязвимость, скорее всего, не закрыта. Но успех не гарантирован.
*   `❌ ВЕРЯТНОСТЬ УСПЕХА НИЗКАЯ`: Уровень патча безопасности новее даты предполагаемого исправления. Шансы на успешную разблокировку малы.
*   `❌ ВЫСОКИЙ РИСК КИРПИЧА`: Обнаружены признаки механизма Anti-Rollback. Попытка разблокировки может привести к необратимой поломке устройства.
*   `⚠️ ...`: Другие предупреждения (отсутствие root, неподдерживаемый чипсет).

## Важные советы

*   **НЕ ОБНОВЛЯЙТЕ** устройство по OTA, если планируете разблокировку. Обновления часто закрывают уязвимости
*   Перед любыми действиями **ПОЛНОСТЬЮ СОХРАНИТЕ** (сделайте дамп) текущую прошивку, если это возможно.
*   Помните, что **даже успешная разблокировка** может сделать устройство более уязвимым для атак.

## Сборка из исходников

Клонируйте репозиторий, откройте проект в Android Studio и соберите APK.

## Благодарности

*   Автор эксплойта [CVE-2022-38694_unlock_bootloader](https://github.com/TomKing062/CVE-2022-38694_unlock_bootloader)
*   Сообществам XDA и GitHub за исследования и обмен информацией.

## Лицензия

MIT License

Copyright (c) 2025 KSN

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
