/*
 * Copyright 2020 Yurii Serhiichuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.xsavikx.androidscreencast.api.command.factory;

import com.github.xsavikx.androidscreencast.api.command.KeyCommand;
import com.github.xsavikx.androidscreencast.api.command.SwipeCommand;
import com.github.xsavikx.androidscreencast.api.command.TapCommand;
import com.github.xsavikx.androidscreencast.api.command.DragAndDropCommand;
import com.github.xsavikx.androidscreencast.api.injector.InputKeyEvent;

public interface InputCommandFactory {

    KeyCommand getKeyCommand(int keyCode);

    KeyCommand getKeyCommand(InputKeyEvent inputKeyEvent, boolean longpress);

    SwipeCommand getSwipeCommand(int x1, int y1, int x2, int y2, long duration);

    TapCommand getTapCommand(int x, int y);

    DragAndDropCommand getDragAndDropCommand(int x1, int y1, int x2, int y2, long duration);
}
