package org.densy.scriptify.common.script.module;

import org.densy.scriptify.api.script.constant.ScriptConstant;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.common.script.function.impl.crypto.ScriptFunctionBase64Decode;
import org.densy.scriptify.common.script.function.impl.crypto.ScriptFunctionBase64Encode;
import org.densy.scriptify.common.script.function.impl.crypto.ScriptFunctionMD5;
import org.densy.scriptify.common.script.function.impl.crypto.ScriptFunctionSHA256;
import org.densy.scriptify.common.script.function.impl.file.*;
import org.densy.scriptify.common.script.function.impl.os.ScriptFunctionEnv;
import org.densy.scriptify.common.script.function.impl.os.ScriptFunctionExecCommand;
import org.densy.scriptify.common.script.function.impl.random.*;
import org.densy.scriptify.common.script.function.impl.util.*;
import org.densy.scriptify.common.script.function.impl.zip.ScriptFunctionSmartUnzipFile;
import org.densy.scriptify.common.script.function.impl.zip.ScriptFunctionSmartZipFile;
import org.densy.scriptify.common.script.function.impl.zip.ScriptFunctionUnzipFile;
import org.densy.scriptify.common.script.function.impl.zip.ScriptFunctionZipFile;
import org.densy.scriptify.core.script.constant.impl.ScriptConstantBaseDir;
import org.densy.scriptify.core.script.constant.impl.ScriptConstantOsName;
import org.densy.scriptify.core.script.module.AbstractScriptInternalModule;
import org.densy.scriptify.core.script.module.export.ScriptConstantExport;
import org.densy.scriptify.core.script.module.export.ScriptFunctionExport;
import org.jetbrains.annotations.NotNull;

public final class StandardScriptModule extends AbstractScriptInternalModule {

    public StandardScriptModule() {
        // exports for functions
        this.export(new ScriptFunctionPrint());
        this.export(new ScriptFunctionExistsFile());
        this.export(new ScriptFunctionDeleteFile());
        this.export(new ScriptFunctionMoveFile());
        this.export(new ScriptFunctionListFiles());
        this.export(new ScriptFunctionReadFile());
        this.export(new ScriptFunctionWriteFile());
        this.export(new ScriptFunctionZipFile());
        this.export(new ScriptFunctionUnzipFile());
        this.export(new ScriptFunctionSmartZipFile());
        this.export(new ScriptFunctionSmartUnzipFile());
        this.export(new ScriptFunctionNormalizePath());
        this.export(new ScriptFunctionBase64Encode());
        this.export(new ScriptFunctionBase64Decode());
        this.export(new ScriptFunctionDownloadFromUrl());
        this.export(new ScriptFunctionJoinPath());
        this.export(new ScriptFunctionRandomUUID());
        this.export(new ScriptFunctionRandomInteger());
        this.export(new ScriptFunctionRandomLong());
        this.export(new ScriptFunctionRandomFloat());
        this.export(new ScriptFunctionRandomDouble());
        this.export(new ScriptFunctionRandomBoolean());
        this.export(new ScriptFunctionMD5());
        this.export(new ScriptFunctionSHA256());
        this.export(new ScriptFunctionExecCommand());
        this.export(new ScriptFunctionEnv());
        this.export(new ScriptFunctionShuffleArray());
        this.export(new ScriptFunctionListOf());
        this.export(new ScriptFunctionSetOf());
        this.export(new ScriptFunctionArrayOf());
        this.export(new ScriptFunctionRegexPattern());
        this.export(new ScriptFunctionRegexMatch());

        // exports for constants
        this.export(new ScriptConstantOsName());
        this.export(new ScriptConstantBaseDir());
    }

    @Override
    public @NotNull String getName() {
        return "standard";
    }

    private void export(ScriptFunction function) {
        this.export(new ScriptFunctionExport(function));
    }

    private void export(ScriptConstant constant) {
        this.export(new ScriptConstantExport(constant));
    }
}
