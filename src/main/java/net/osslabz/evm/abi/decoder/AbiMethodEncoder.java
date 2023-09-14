package net.osslabz.evm.abi.decoder;

import net.osslabz.evm.abi.definition.AbiDefinition;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author QiuDie
 * @date 2023/09/15
 * @description AbiMethodEncoder makes use of the Contract ABI and method name and parameters value to encode the input data.
 */
public class AbiMethodEncoder {
    private  AbiDefinition abi;
    private Map<String, AbiDefinition.Entry> methodEntryMap = new HashMap();
    private final static String HEX_PREFIX = "0x";

    public AbiMethodEncoder(String abiFilePath) throws IOException {
        this.abi = AbiDefinition.fromJson(Files.readString(Path.of(abiFilePath)));
        init();
    }

    public void AbiMethodEncoder(InputStream inputStream) {
        this.abi = AbiDefinition.fromJson(inputStream);
        init();
    }

    public void AbiMethodEncoder(String abiJson) {
        this.abi = AbiDefinition.fromJson(abiJson);
        init();
    }



    public String encodeMethod(String methodName, Object... args) {
        AbiDefinition.Entry entry = this.methodEntryMap.get(methodName);
        if (entry == null) {
            throw new RuntimeException("Method " + methodName + " not found");
        } else {
            AbiDefinition.Function function = (AbiDefinition.Function)entry;
            byte[] signature = function.encode(args);

            return HEX_PREFIX+ Hex.toHexString(signature);
        }
    }


    private void init() {
        Iterator var1 = this.abi.iterator();

        while(var1.hasNext()) {
            AbiDefinition.Entry entry = (AbiDefinition.Entry) var1.next();
            this.methodEntryMap.put(entry.name, entry);
        }
    }
}
