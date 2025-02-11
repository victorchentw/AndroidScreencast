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

package com.github.xsavikx.androidscreencast.ui.worker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An abstract class to be used in the cases where we need {@code Runnable} to perform some actions on an appendable set of data. The set of data
 * might be appended after the {@code Runnable} is sent for the execution. Usually such {@code Runnables} are sent to the EDT.
 * <p>
 * <p>
 * Usage example:
 * <p>
 * <p>
 * Say we want to implement JLabel.setText(String text) which sends {@code text} string to the JLabel.setTextImpl(String text) on the EDT. In the
 * event JLabel.setText is called rapidly many times off the EDT we will get many updates on the EDT but only the last one is important. (Every next
 * updates overrides the previous one.) We might want to implement this {@code setText} in a way that only the last update is delivered.
 * <p>
 * Here is how one can do this using {@code AccumulativeRunnable}:
 * <p>
 * <pre>
 * AccumulativeRunnable<String> doSetTextImpl =
 * new  AccumulativeRunnable<String>() {
 *     &#64;Override
 *     protected void run(List&lt;String&gt; args) {
 *         //set to the last string being passed
 *         setTextImpl(args.get(args.size() - 1);
 *     }
 * }
 * void setText(String text) {
 *     //add text and send for the execution if needed.
 *     doSetTextImpl.add(text);
 * }
 * </pre>
 * <p>
 * <p>
 * Say we want want to implement addDirtyRegion(Rectangle rect) which sends this region to the handleDirtyRegions(List<Rect> regions) on the EDT.
 * addDirtyRegions better be accumulated before handling on the EDT.
 * <p>
 * <p>
 * Here is how it can be implemented using AccumulativeRunnable:
 * <p>
 * <pre>
 * AccumulativeRunnable&lt;Rectangle&gt; doHandleDirtyRegions = new AccumulativeRunnable&lt;Rectangle&gt;() {
 *   &#064;Override
 *   protected void run(List&lt;Rectangle&gt; args) {
 *     handleDirtyRegions(args);
 *   }
 * };
 *
 * void addDirtyRegion(Rectangle rect) {
 *   doHandleDirtyRegions.add(rect);
 * }
 * </pre>
 *
 * @param <T> the type this {@code Runnable} accumulates
 * @author Igor Kushnirskiy
 * @version $Revision: 1.3 $ $Date: 2008/07/25 19:32:29 $
 */
abstract class AccumulativeRunnable<T> implements Runnable {
    private List<T> arguments = null;

    /**
     * prepends or appends arguments and sends this {@code Runnable} for the execution if needed.
     * <p>
     * This implementation uses {@see #submit} to send this {@code Runnable} for execution.
     *
     * @param isPrepend prepend or append
     * @param args      the arguments to add
     */
    public final synchronized void add(boolean isPrepend, T... args) {
        boolean isSubmitted = true;
        if (arguments == null) {
            isSubmitted = false;
            arguments = new ArrayList<>();
        }
        if (isPrepend) {
            arguments.addAll(0, Arrays.asList(args));
        } else {
            Collections.addAll(arguments, args);
        }
        if (!isSubmitted) {
            submit();
        }
    }

    /**
     * appends arguments and sends this {@code Runnable} for the execution if needed.
     * <p>
     * This implementation uses {@see #submit} to send this {@code Runnable} for execution.
     *
     * @param args the arguments to accumulate
     */
    public final void add(T... args) {
        add(false, args);
    }

    /**
     * Returns accumulated arguments and flashes the arguments storage.
     *
     * @return accumulated arguments
     */
    private synchronized List<T> flush() {
        List<T> list = arguments;
        arguments = null;
        return list;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * This implementation calls {@code run(List<T> args)} method with the list of accumulated arguments.
     */
    @Override
    public final void run() {
        run(flush());
    }

    /**
     * Equivalent to {@code Runnable.run} method with the accumulated arguments to process.
     *
     * @param args accumulated arguments to process.
     */
    protected abstract void run(List<T> args);

    /**
     * Sends this {@code Runnable} for the execution
     * <p>
     * <p>
     * This method is to be executed only from {@code add} method.
     * <p>
     * <p>
     * This implementation uses {@code SwingWorker.invokeLater}.
     */
    protected void submit() {
        SwingUtilities.invokeLater(this);
    }
}
