package io.jprime.agenticrag.retriever.domain.llm.tool;

import io.jprime.agenticrag.retriever.domain.service.KnowledgeBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KnowledgeBaseQueryTools {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseQueryTools.class);

    private final KnowledgeBaseService knowledgeBaseService;

    public KnowledgeBaseQueryTools(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    @Tool(description = """
            Searches the knowledge base for information about video editing cards and systems.
            Use this tool when the user asks about:
            - Technical specifications and features of a specific card or editing system
            - Hardware requirements and system compatibility (CPU type, RAM minimums,
            bus slot type such as PCI or 16-bit ISA, IRQ requirements, OS version)
            - Video input/output capabilities (composite, component, Y/C, SDI, FireWire,
            HD Serial Digital, analog connections, VGA output)
            - Audio capabilities (number of channels, mixing, EQ, sample rates,
            AES/EBU, balanced/unbalanced, embedded SDI audio)
            - Supported video standards and formats (NTSC, PAL, SD, HD 1080i, 720p,
            4:3, 16:9, interlaced, progressive)
            - Compression codecs and quality settings (MJPEG, DV, DVCPro, uncompressed
            8-bit or 10-bit, draft quality modes, data rates in MB/sec)
            - Real-time effects, transitions, keying, compositing and rendering capabilities
            - Web streaming and export formats (MPEG-1, MPEG-2, RealVideo, Windows Media,
            QuickTime, AVI)
            - Bundled software and plug-ins (titling, DVD authoring, audio sweetening,
            compositing tools)
            - Driver installation, configuration, port addresses, memory mapping
            - Cable connections and peripheral equipment requirements (VGA boards,
            TV monitors, VCRs, cameras, antenna)
            - Licensing, ordering and registration procedures
            - Project setup, media destinations, preference configurations
            - Format conversion options (pillarbox, letterbox, crop, scale to fit)
            - Disk space and throughput requirements for different quality levels
            Use this tool whenever the question is related to a specific video editing
            card or NLE system by name, even if the question is about the hardware
            or software environment it runs on.
            """)
    public List<String> searchKnowledgeBase(String query) {
        log.info("[Tool:local] searchKnowledgeBase called — query: '{}'", query);

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Knowledge Base search query cannot be blank.");
        }

        List<String> results = knowledgeBaseService.search(query);
        log.info("[Tool:local] searchKnowledgeBase returned {} result(s)", results.size());

        return results;
    }
}
