/*
 * This file is part of Ignite, licensed under the MIT License (MIT).
 *
 * Copyright (c) vectrix.space <https://vectrix.space/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package space.vectrix.ignite.game;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import space.vectrix.ignite.Blackboard;
import space.vectrix.ignite.IgniteBootstrap;
import space.vectrix.ignite.agent.IgniteAgent;
import space.vectrix.ignite.util.BlackboardMap;

/**
 * Provides a game locator for PowernukkitX.
 *
 * @author Kanelucky
 * @since 1.2.1
 */
public class PowernukkitXGameLocator implements GameLocatorService {
  private static final BlackboardMap.@NotNull Key<Path> POWERNUKKITX_JAR = Blackboard.key("ignite.powernukkitx.jar",Path.class, Paths.get("./powernukkitx.jar"));
  public static final BlackboardMap.@NotNull Key<String> GAME_TARGET = Blackboard.key("ignite.target", String.class, "cn.nukkit.Nukkit");

  private PowernukkitXGameProvider provider;

  @Override
  public @NotNull String id() {
    return "powernukkitx";
  }

  @Override
  public @NotNull String name() {
    return "PowernukkitX";
  }

  @Override
  public int priority() {
    return 1;
  }

  @Override
  public boolean shouldApply() {
    return true;
  }

  @Override
  public void apply(final @NotNull IgniteBootstrap bootstrap) throws Throwable {
    Blackboard.compute(PowernukkitXGameLocator.POWERNUKKITX_JAR, () -> Paths.get(System.getProperty(PowernukkitXGameLocator.POWERNUKKITX_JAR.name())));

    this.provider = new PowernukkitXGameProvider();

    try {
      IgniteAgent.addJar(Blackboard.raw(PowernukkitXGameLocator.POWERNUKKITX_JAR));
    } catch(final IOException exception) {
      throw new IllegalStateException("Unable to add powernukkitx jar to classpath!", exception);
    }
  }

  @Override public @NotNull GameProvider locate() {
    return this.provider;
  }

  static final class PowernukkitXGameProvider implements GameProvider {
    /* package */ PowernukkitXGameProvider() {
    }

    @Override
    public @NotNull Stream<Path> gameLibraries() {
      return Stream.empty();
    }

    @Override
    public @NotNull Path gamePath() {
      return Blackboard.get(Blackboard.GAME_JAR).orElseGet(() -> Paths.get("./powernukkitx.jar"));
    }
  }
}
